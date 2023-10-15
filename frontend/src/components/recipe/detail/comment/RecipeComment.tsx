import React, { useState, useEffect, useRef, useCallback } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/FetchCall'
import CommonButton from '../../../ui/Button'
import Dialog from '../../../ui/Dialog'

interface CommentItem {
  id: number
  message: string
  isDeleted: boolean
  nickname: string
  createdAt: string
  profileUrl: string
}

const RecipeComment = () => {
  const [recipeId, setRecipeId] = useState(10)
  const [commentValue, setCommentValue] = useState('')
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [comment, setComment] = useState({})
  const [commentId, setCommentId] = useState(0)
  const [pageIdx, setPageIdx] = useState(0)
  const pageSize = useRef(5)
  const endOfList = useRef(false)
  const [editValue, setEditValue] = useState('')
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)
  const [isEditing, setIsEditing] = useState(false)

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.currentTarget.value)
  }

  // post
  const onSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    const body = {
      message: commentValue,
      recipeId,
    }

    try {
      const res = await axiosInstance.post(`/comment/${recipeId}`, body)
      if (res.status === 200) {
        console.log('작성한다', res.config.data)
        setCommentValue('')
        setPageIdx(1)
      }
    } catch (error) {
      console.error('새 댓글 등록 중 오류 발생:', error)
    }
  }

  // get
  const getComment = useCallback(async () => {
    const res = await axiosInstance.get(
      `/comment/${recipeId}/${pageIdx}/${pageSize.current}`
    )

    if (res.status === 200) {
      const commentData = res.data.content
      const messages = commentData.map((commentItem: CommentItem) => {
        try {
          const parsedComment = JSON.parse(commentItem.message)
          if (parsedComment) {
            return parsedComment
          }
          return commentItem.message
        } catch (error) {
          return commentItem.message
        }
      })

      if (messages.length === -1) {
        endOfList.current = true
      } else if (pageIdx === 0) {
        setCommentList(messages)
      } else {
        setCommentList(prevCommentList => [...prevCommentList, ...messages])
      }
    }
  }, [recipeId, pageIdx])

  useEffect(() => {
    getComment()
  }, [getComment])

  const handleLoadMoreComments = () => {
    if (!endOfList.current) {
      setPageIdx(prevPageIdx => prevPageIdx + 1)
    }
  }

  // 수정 버튼 누르면
  const handleEditComment = (commentIdParams: number) => {
    console.log('댓글 ID:', commentIdParams)
    setCommentId(commentIdParams)
    setIsEditing(true)
  }

  // 수정 -> 확인 버튼 누르면
  const handleConfirmEdit = async () => {
    try {
      const body = {
        modifiedMessage: editValue,
      }
      const res = await axiosInstance.put(`/comment/${commentId}`, body)
      if (res.status === 200) {
        console.log('댓글 업데이트 완료')
        setIsEditing(false)
        setEditValue('')
      }
    } catch (error) {
      console.error('댓글 업데이트 실패', error)
    }
  }

  // 댓글 수정 취소
  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditValue('')
  }

  // 삭제 버튼 누르면
  const handleDeleteComment = (commentIdParams: number) => {
    setIsDeleteModalOpen(true)
    console.log('삭제버튼 누름', commentIdParams)
  }

  // 댓글을 삭제하시겠습니까? 확인 버튼
  const handleConfirmDelete = async () => {
    const res = await axiosInstance.delete(`/comment/${recipeId}/${commentId}`)
    try {
      if (res.status === 200) {
        console.log('댓글을 삭제하였습니다.')
      }
    } catch (error) {
      console.error('삭제 안되서 에러남', error)
    } finally {
      setIsDeleteModalOpen(false)
    }
  }

  return (
    <S.CommentContainer>
      <S.CommentTit>댓글</S.CommentTit>
      <S.CommentForm>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value={commentValue}
        />
        <S.CommentSubmit onClick={onSubmit}>등록</S.CommentSubmit>
      </S.CommentForm>
      <S.CommentList>
        {commentList.map((commentItem, idx) => (
          <div key={commentItem.id || idx}>
            <S.CommentItemWrapper>
              <S.CommentHeader>
                <S.CommentUserInfo>
                  <S.UserImg
                    src={commentItem.profileUrl}
                    alt={commentItem.nickname}
                  />
                  <S.UserNickname>{commentItem.nickname}</S.UserNickname>
                </S.CommentUserInfo>
                <S.CommentDate>{commentItem.createdAt}</S.CommentDate>
              </S.CommentHeader>

              {isEditing ? (
                <S.CommentEdit>
                  <S.CommentEditInput
                    value={editValue}
                    onChange={e => setEditValue(e.target.value)} // setEditValue를 사용하여 editValue를 업데이트
                  />
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={handleConfirmEdit} // 함수를 호출하여 수정 내용을 서버에 업데이트
                    >
                      확인
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={handleCancelEdit} // 함수를 호출하여 수정을 취소
                    >
                      취소
                    </CommonButton>
                  </S.CommentButtonWrap>
                </S.CommentEdit>
              ) : (
                <>
                  <S.CommentContent>{commentItem.message}</S.CommentContent>
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleEditComment(commentItem.id)} // 함수를 호출하여 수정 모드로 전환
                    >
                      수정
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleDeleteComment(commentItem.id)} // 함수를 호출하여 모달 오픈
                    >
                      삭제
                    </CommonButton>
                  </S.CommentButtonWrap>
                </>
              )}
            </S.CommentItemWrapper>
          </div>
        ))}
      </S.CommentList>
      <S.MoreButtonBox>
        <CommonButton
          onClick={handleLoadMoreComments}
          color="main"
          border="border"
        >
          댓글 더보기
        </CommonButton>
      </S.MoreButtonBox>
      {isDeleteModalOpen && (
        <Dialog
          title="댓글 삭제 확인"
          confirmText="확인"
          cancelText="취소"
          onConfirm={handleConfirmDelete} // 모달에서 삭제 확인
          onCancel={() => setIsDeleteModalOpen(false)} // 모달 close
          visible={isDeleteModalOpen}
        >
          댓글을 삭제하시겠습니까?
        </Dialog>
      )}
    </S.CommentContainer>
  )
}

export default RecipeComment
