import React, { useState, useEffect, useRef, useCallback } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/fetchCall'
import CommonButton from '../../../ui/Button'
import Dialog from '../../../ui/Dialog'
import { CommentItem } from '../../../../constants/Interfacs'

function RecipeComment({ recipeId }: { recipeId: number }) {
  const [commentValue, setCommentValue] = useState('')
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [pageIdx, setPageIdx] = useState(1)
  const pageSize = useRef(5)
  const endOfList = useRef(false)
  const [isEditing, setIsEditing] = useState(false)
  const [editValue, setEditValue] = useState('')
  const [commentId, setCommentId] = useState('')
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.currentTarget.value)
  }

  const onSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    const body = {
      message: commentValue,
      recipeId: 1,
    }

    try {
      const res = await axiosInstance.post('/comment/10', body)
      if (res.status === 200) {
        console.log('작성한다', res.config.data)
        setCommentValue('')
        setPageIdx(1)
      }
    } catch (error) {
      console.error('Error adding a new comment:', error)
    }
  }

  const getComment = useCallback(async () => {
    const res = await axiosInstance.get(
      `/comment/10/${pageIdx}/${pageSize.current}`
    )
    if (res.status === 200) {
      const comment = res.data.commentDtoList

      const messages = comment.map((commentItem: CommentItem) => {
        const parsedComment = JSON.parse(commentItem.message)
        return parsedComment || commentItem
      })

      if (messages.length === 0) {
        endOfList.current = true
      } else if (pageIdx === 1) {
        setCommentList(messages)
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

  // 댓글을 수정하는 동작을 시작
  const handleEditComment = (commentId: string, message: string) => {
    setIsEditing(true)
    setEditValue(message)
    setCommentId(commentId)
  }

  // 실제로 서버에 업데이트하는 역할
  const handleConfirmEdit = async (commentId: string) => {
    try {
      const body = {
        message: editValue,
      }
      const res = await axiosInstance.put(`/comment/10/${commentId}`, body)
      if (res.status === 200) {
        const updatedComments = commentList.map(comment =>
          String(comment.commentId) === commentId
            ? { ...comment, message: editValue }
            : comment
        )
        console.log('업데이트 하였습니다.')
        setCommentList(updatedComments)
        setIsEditing(false)
        setEditValue('')
        setCommentId('')
      }
    } catch (error) {
      console.error('Error editing the comment:', error)
    }
  }

  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditValue('')
    setCommentId('')
  }

  // 댓글 삭제를 확인하기 위해 모달 대화 상자를 열 때 호출
  const handleConfirmDelete = async () => {
    const res = await axiosInstance.delete(`/comment/10/${commentId}`)
    try {
      if (res.status === 200) {
        console.log('댓글을 삭제하였습니다.')
        const updatedComments = commentList.filter(
          comment => String(comment.commentId) !== commentId
        )
        setCommentList(updatedComments)
      }
    } catch (error) {
      console.error('Error deleting the comment:', '삭제가안됨')
    } finally {
      // 모달 닫기
      setIsDeleteModalOpen(false)
    }
  }

  // 실제 삭제 동작을 담당
  const handleDeleteComment = (commentId: string) => {
    if (commentId) {
      setIsDeleteModalOpen(true)
      setCommentId(commentId)
    } else {
      console.error('Invalid commentId')
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
          <div key={commentItem.commentId || index}>
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

              {isEditing && String(commentItem.commentId) === commentId ? (
                // 수정 모드일 때
                <div>
                  <input
                    type="text"
                    value={editValue}
                    onChange={e => setEditValue(e.target.value)}
                  />
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={() =>
                      handleConfirmEdit(String(commentItem.commentId))
                    }
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
                </div>
              ) : (
                // 수정 모드가 아닐 때
                <>
                  <S.CommentContent>{commentItem.message}</S.CommentContent>
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() =>
                        handleEditComment(
                          String(commentItem.commentId),
                          commentItem.message
                        )
                      }
                    >
                      수정
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() =>
                        handleDeleteComment(String(commentItem.commentId))
                      }
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
