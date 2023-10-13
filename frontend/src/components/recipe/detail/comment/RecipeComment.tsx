import React, { useState, useEffect, useRef, useCallback } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/fetchCall'
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

const RecipeComment = recipeId => {
  const [commentValue, setCommentValue] = useState('')
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [pageIdx, setPageIdx] = useState(0)
  const pageSize = useRef(5)
  const endOfList = useRef(false)
  const [editValue, setEditValue] = useState('')
  const [commentId, setCommentId] = useState(0)
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
      recipeId: 1,
    }

    try {
      const res = await axiosInstance.post(`/comment/10`, body)
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
      `/comment/10/${pageIdx}/${pageSize.current}`
    )
    console.log('뭘 받아오지?', res)
    if (res.status === 200) {
      const comment = res.data.content
      res.data.content.forEach((comment: CommentItem) => {
        // commentId는 comment.id
        const commentId = comment.id
        console.log('댓글 ID:', commentId)
        setCommentId(commentId)
      })

      // message 값만 나오게 parse
      const messages = comment.map((commentItem: CommentItem) => {
        const parsedComment = JSON.parse(commentItem.message)
        return parsedComment || commentItem
      })

      if (messages.length === -1) {
        endOfList.current = true
      } else if (pageIdx === 0) {
        setCommentList(messages)
        // console.log('메세지는?', messages)
      } else {
        setCommentList(prevCommentList => [...prevCommentList, ...messages])
      }
    }
  }, [pageIdx])

  useEffect(() => {
    getComment()
  }, [getComment])

  const handleLoadMoreComments = () => {
    if (!endOfList.current) {
      setPageIdx(prevPageIdx => prevPageIdx + 1)
    }
  }

  // 수정 버튼 누르면
  const handleEditComment = (commentId: number) => {
    console.log('댓글 ID:', commentId)
    setIsEditing(true)
    setCommentId(commentId)
  }

  // 수정 -> 확인 버튼 누르면
  const handleConfirmEdit = async () => {
    try {
      const body = {
        message: editValue,
      }
      const res = await axiosInstance.put(`/comment/10/${commentId}`, body)
      if (res.status === 200) {
        console.log('업데이트 하였습니다.')
      }
    } catch (error) {
      console.error('업데이트 실패', error)
    }
  }

  // 댓글 수정 취소
  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditValue('')
  }

  // 삭제 버튼 누르면
  const handleDeleteComment = () => {
    setIsDeleteModalOpen(true)
    console.log('삭제버튼 누름')
  }

  // 댓글을 삭제하시겠습니까? 확인 버튼
  const handleConfirmDelete = async (index: number, commentId: number) => {
    const res = await axiosInstance.delete(`/comment/10/${commentId}`)
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
        {commentList.map((commentItem, index) => (
          <div key={commentItem.id || index}>
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
                    onChange={e => setEditValue(e.target.value)}
                  />
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleConfirmEdit()}
                    >
                      확인
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={handleCancelEdit}
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
                      onClick={() => handleEditComment(commentItem.id)}
                    >
                      수정
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleDeleteComment()}
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
          onConfirm={handleConfirmDelete}
          onCancel={() => setIsDeleteModalOpen(false)}
          visible={isDeleteModalOpen}
        >
          댓글을 삭제하시겠습니까?
        </Dialog>
      )}
    </S.CommentContainer>
  )
}

export default RecipeComment
