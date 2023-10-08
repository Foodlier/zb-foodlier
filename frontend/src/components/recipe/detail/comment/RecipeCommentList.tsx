import React, { useState, useEffect } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeCommentList.styled'
import RecipeCommentItem from './RecipeCommentItem'
import RecipeCommentForm from './RecipeCommentForm'
import allDummyComments from './allDummyComments'
import CommonButton from '../../../ui/Button'

interface Comment {
  commentId: string
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
  referenceId: string
  isReply: boolean
  replies?: Comment[]
}

interface CommentListProps {
  recipeId: string | number
}

const RecipeCommentList: React.FC<CommentListProps> = ({ recipeId }) => {
  const [comments, setComments] = useState<Comment[] | undefined>([])
  const [visibleComments, setVisibleComments] = useState<Comment[]>([])
  const [showLoadMoreComments, setShowLoadMoreComments] = useState(false)

  useEffect(() => {
    setComments(allDummyComments)
  }, [recipeId])

  useEffect(() => {
    if (comments === undefined) return

    const initialComments = comments.slice(0, 5)
    setVisibleComments(initialComments)
    setShowLoadMoreComments(comments.length > initialComments.length)
  }, [comments])

  // 삭제
  const handleCommentDeleted = (commentId: string) => {
    if (comments === undefined) return

    const updatedComments = comments.map(comment =>
      comment.commentId === commentId
        ? { ...comment, isDeleted: true }
        : comment
    )
    setComments(updatedComments)
  }

  // 수정
  const handleCommentEdited = (commentId: string, updatedMessage: string) => {
    if (comments === undefined) return

    const updatedComments = comments.map(comment =>
      comment.commentId === commentId
        ? { ...comment, message: updatedMessage }
        : comment
    )
    setComments(updatedComments)
  }
  // 등록
  const handleNewCommentSubmit = (newComment: string) => {
    if (newComment.trim() === '') {
      return
    }
    const newCommentId = (comments ? comments.length : 0).toString()
    const newCommentItem: Comment = {
      commentId: newCommentId,
      message: newComment,
      createdAt: new Date().toISOString(),
      isDeleted: false,
      nickname: '사용자',
      profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      referenceId: '-1',
      isReply: false,
      replies: [],
    }

    const updatedComments = [newCommentItem, ...(comments || [])]
    setComments(updatedComments)
  }

  // 댓글 더보기 버튼
  const handleLoadMoreComments = () => {
    if (comments === undefined) return
    const remainingComments = comments.slice(visibleComments.length)
    const nextVisibleComments = remainingComments.slice(0, 5)
    setVisibleComments(prevComments => [
      ...prevComments,
      ...nextVisibleComments,
    ])
    setShowLoadMoreComments(
      remainingComments.length > nextVisibleComments.length
    )
  }
  return (
    <S.RecipeCommentList>
      <S.CommentTit>댓글</S.CommentTit>
      <RecipeCommentForm onSubmit={handleNewCommentSubmit} />

      {visibleComments.map(comment => (
        <RecipeCommentItem
          key={comment.commentId}
          comment={comment}
          onDelete={handleCommentDeleted}
          onEdit={handleCommentEdited}
        />
      ))}

      <S.MoreButtonBox>
        {showLoadMoreComments && (
          <CommonButton
            onClick={handleLoadMoreComments}
            color="main"
            border="border"
          >
            댓글 더보기
          </CommonButton>
        )}
      </S.MoreButtonBox>
    </S.RecipeCommentList>
  )
}

export default RecipeCommentList
